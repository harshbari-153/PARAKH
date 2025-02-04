import streamlit as st
import os
import json
import re
import google.generativeai as genai
import pandas as pd
genai.configure(api_key="AIzaSyDJAvS5grnneKNrpSkIG3qPDN_sJtDUi0g")


# Set Page Config for better UI
st.set_page_config(page_title="Review Scoring App", layout="centered")
#################################################################################




#################################################################################
#genai.configure(api_key=os.environ["AIzaSyBjT_lfZk9v3OrgQPvQiOJwhfcmq2ymu_Y"])

# Create the model
generation_config = {
  "temperature": 1,
  "top_p": 0.95,
  "top_k": 40,
  "max_output_tokens": 700,
  "response_mime_type": "application/json",
}

model = genai.GenerativeModel(
  #model_name="gemini-2.0-flash-exp",
  model_name="gemini-1.5-pro",
  generation_config=generation_config,
)

chat_session = model.start_chat(
  history=[
  ]
)
#################################################################################


#################################################################################
def extract_substrings(data):
    # Find the indices of the '"' characters in the string
    quote_indices = [i for i, char in enumerate(data) if char == '"']
    
    # Check if there are at least 8 '"' characters
    if len(quote_indices) < 8:
        # If not, set the second substring to "No claim found"
        second_substring = "No claim found"
    else:
        # Extract the substrings between the 3rd and 4th '"' and between the 7th and 8th '"'
        second_substring = data[quote_indices[6]+1:quote_indices[7]]
    
    # Extract the first substring from the 3rd to 4th '"' (always assumed to exist)
    first_substring = data[quote_indices[2]+1:quote_indices[3]]
    
    # Return the substrings as a list
    return [first_substring, second_substring]



def extract_first_integer(s):
    # Use regex to find the first integer in the string
    match = re.search(r'\d+', s)  # '\d+' matches one or more digits
    if match:
        return int(match.group())  # Return the first match as an integer
    else:
        return 0  # Return None if no integer is found
################################################################################




###############################################################################
def extract_patents(review):
    patient_count_prompt = f"""Given a paragraph, return number of patents (return zero if found none) as a single number in response\n\nparagraph:\n{review}"""

    return extract_first_integer(chat_session.send_message(patient_count_prompt).text)
###############################################################################



###############################################################################
def extract_average_package(review):
    patient_count_prompt = f"""Given a paragraph, return average package in integer format, it may be in LPA or lakh or thousand or any other format, return proper number (return zero if found none) as a single number in response\n\nparagraph:\n{review}"""

    return extract_first_integer(chat_session.send_message(patient_count_prompt).text)
###############################################################################




###############################################################################
def extract_courses(review):
    course_count_prompt = f"""Given a paragraph, return total number of courses or subject it provides (return zero if found none) as a single number in response\n\nparagraph:\n{review}"""

    return extract_first_integer(chat_session.send_message(course_count_prompt).text)
###############################################################################



###############################################################################
def extract_addmission(review):
    course_count_prompt = f"""Given a paragraph, return total number of addmissions (return zero if found none) as a single number in response\n\nparagraph:\n{review}"""

    return extract_first_integer(chat_session.send_message(course_count_prompt).text)
###############################################################################






# Add a title to the app
my_college = "xyz_college"
st.title(my_college)

# Add a subtle divider for better UI
st.markdown("---")

# Create an Edit Text Box (Multiline Input)
user_input = st.text_area("Enter your review below:", height=150)

# Create two columns for buttons (side by side)
col1, col2 = st.columns(2)

# Variables to hold displayed text
recommendation_text = ""
score_text = ""

# NIRF details
df = pd.read_csv("nirf.csv")
sucess = True

# When "Generate Score" is clicked
if col1.button("Generate Score"):
    review_length = len(user_input.strip())


    if 200 <= review_length <= 500:
        #recommendation_text = user_input  # Show entered review as a recommendation

        record = df[df['college_name'] == my_college]
        ########################################
        rev_pat = extract_patents(user_input)
        nirf_pat = record['patents_published'].iloc[0]

        if rev_pat != 0 and abs(rev_pat - nirf_pat) > 2:
            st.error("Your NIRF data (patents) do not matches with review")
            recommendation_text = "Your NIRF data (patents) do not matches with review"
            sucess = False
        ########################################
        rev_pak = extract_average_package(user_input)
        nirf_pak = record['average_packages'].iloc[0]

        if rev_pak != 0 and abs(rev_pak - nirf_pak) > 150000:
            st.error("Your NIRF data (package) do not matches with review")
            recommendation_text = "Your NIRF data (package) do not matches with review"
            sucess = False
        ########################################
        rev_cor = extract_courses(user_input)
        nirf_cor = record['tot_courses'].iloc[0]

        if rev_cor != 0 and abs(rev_cor - nirf_cor) > 2:
            st.error("Your NIRF data (courses) do not matches with review")
            recommendation_text = "Your NIRF data (courses) do not matches with review"
            sucess = False
        ########################################
        rev_add = extract_addmission(user_input)
        nirf_add = record['addmissions'].iloc[0]

        if rev_cor != 0 and abs(rev_cor - nirf_cor) > 10:
            st.error("Your NIRF data (addmissions) do not matches with review")
            recommendation_text = "Your NIRF data (addmissions) do not matches with review"
            sucess = False
        ########################################

        if sucess:
            st.success("Your NIRF data (addmissions) do not matches with review")



	###
        
    else:
        recommendation_text = "❌ Please enter a review between 200 to 500 characters."
        score_text = ""

# When "Post" is clicked
if col2.button("Post"):
    recommendation_text = "✅ Review Posted Successfully"
    score_text = ""

# Divider for UI
st.markdown("---")

# Display the recommendations text
st.markdown("## Recommendations")
st.markdown(f"**{recommendation_text}**")

# Divider for UI
st.markdown("### Score")
st.markdown(f"**{score_text}**" if score_text else "🔢 No score available")

# Footer style
st.markdown("<br><hr><center>Made with ❤️ in Streamlit</center>", unsafe_allow_html=True)
