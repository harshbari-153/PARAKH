# Parakh - Unambigious College Review Platform

## Introduction
In many college review websites, exaggerated and ambiguous reviews mislead students into making incorrect decisions. Colleges often post generic statements such as _"Our college ranks high in research and development,"_ which are neither completely true nor completely false. This results in unaccountable and unverifiable reviews.

Our platform aims to ensure that reviews are clear, justified, and accountable. For example, instead of vague claims, we expect concrete statements like _"Our college has published 100 research papers in the last year,"_ which are verifiable and fact-based.

## Problem Statement
Many review platforms allow colleges and other organizations to post exaggerated or misleading claims without any accountability. This misguides students and other users while the institutions remain unaffected.

### Gap
The primary reason for misleading reviews is the use of generalized claims rather than specific, verifiable information. Our platform addresses this issue by ensuring that every review is backed by proper justification and receives a **justification score (trust score)**.

## Our Approach
We are developing a **website** and a **mobile application** that aim to:
1. **Extract claims from a given review**
2. **Find justification for specific claims**
3. **Measure the contextual similarity between claims and their justifications (on a scale of 0 to 100)**
4. **Assign an average similarity score to each review**

The **similarity score** will act as a **justification score (trust score)** associated with each review.

## Expected Outcomes
- Organizations (colleges, hotels, restaurants, companies, etc.) will be held accountable for their reviews.
- No exaggerated or ambiguous reviews will exist on our platform.
- Institutions will need to provide factual and verifiable information in their reviews.
- Users will have enhanced **trust and confidence** while making decisions.

## Tech Stack
Our platform is built using the following technologies:
- **Android** (Java) – for mobile application
- **Firebase** – for database and authentication
- **Next.js** – for web application development
- **Streamlit** – for review analysis and visualization

## Contributors
This project, **Parakh**, was created during the **Dotslash 8.0** hackathon at SVNIT Surat by the following contributors:
1. Ajit Kumar
2. Nikhil Gajbhiye
3. Harsh Bari

## Getting Started
### Prerequisites
- Install Node.js and npm
- Install Java and Android Studio for mobile development
- Set up Firebase for authentication and database

### Installation
#### Clone the Repository:
```sh
 git clone https://github.com/harshbari-153/PARAKH.git
 cd PARAKH
```
#### Install Dependencies (for Web Application):
```sh
 cd web-app
 npm install
 npm run dev
```
#### Running the Streamlit Analysis Tool:
```sh
 cd analysis-tool
 streamlit run app.py
 streamlit run app_2.py
```

### Contributing
We welcome contributions from the community! Feel free to open issues, submit pull requests, or suggest improvements.

---
Thank you for checking out Parakh! 🚀

