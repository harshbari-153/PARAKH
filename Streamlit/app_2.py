import os
import google.generativeai as genai
import json
import streamlit as st
import time

#genai.configure(api_key=os.getenv("AIzaSyBjT_lfZk9v3OrgQPvQiOJwhfcmq2ymu_Y"))
genai.configure(api_key="AIzaSyDJAvS5grnneKNrpSkIG3qPDN_sJtDUi0g")

# Streamlit UI
st.title("Claim Extraction & Justification Matching")

review_text = st.text_area("Enter your review text:")
if st.button("Proceed"):
    if not review_text.strip():
        st.error("Please enter a review text before proceeding.")
    else:
        generation_config = {
            "temperature": 1,
            "top_p": 0.95,
            "top_k": 40,
            "max_output_tokens": 500,
            "response_mime_type": "application/json",
        }

        model = genai.GenerativeModel(
            model_name="gemini-2.0-flash-exp",
            generation_config=generation_config,
        )

        chat_session = model.start_chat(history=[])
        
        time.sleep(10)

        extract_claim_prompt = f"""Extract top 2 claims from given review from which the person is concluding something, return them in 2 points in this manner
        1: conclusion 1
        2: conclusion 2
        return them in dictionary format with key as \"0\" and \"1\", both are string type, each claim must be in minimum words (not more than 6 words)

        given review:\n{review_text}"""

        claims = chat_session.send_message(extract_claim_prompt)
        claims = json.loads(claims.text)
        st.success(f"Claim 1: {claims['0']}")
        st.success(f"Claim 2: {claims['1']}")
        
        time.sleep(10)

        extract_justification = lambda claim: f"""Given a claim and a review, extract relevant justification which are supported by figures and numbers only.
        If no such relevant justification is found, return \"No Relevant Justification Found\".
        Return the justification in maximum 6 words, as a dictionary with key as \"justification\".

        claim:
        {claim}

        review: {review_text}"""

        just_0 = json.loads(chat_session.send_message(extract_justification(claims["0"])).text)
        time.sleep(10)
        just_1 = json.loads(chat_session.send_message(extract_justification(claims["1"])).text)

        st.success(f"Justification 1: {just_0['justification']}")
        st.success(f"Justification 2: {just_1['justification']}")
        
        time.sleep(10)

        similarity_prompt = lambda claim, justification: f"""Given a claim and justification, give an integer score from 0 to 100 for how much they align.
        Return the score in a dictionary with key \"score\".

        claim:
        {claim}

        justification:
        {justification}"""

        sim_0 = int(json.loads(chat_session.send_message(similarity_prompt(claims["0"], just_0["justification"])).text)["score"])
        time.sleep(10)
        sim_1 = int(json.loads(chat_session.send_message(similarity_prompt(claims["1"], just_1["justification"])).text)["score"])

        st.success(f"Similarity Score 1: {sim_0}")
        st.success(f"Similarity Score 2: {sim_1}")
        
        time.sleep(10)

        avg_score = round((sim_0 + sim_1) / 2, 2)
        st.success(f"Average Justification Score: {avg_score}")
