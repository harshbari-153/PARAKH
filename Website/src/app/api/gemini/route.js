// src/app/api/geminiAPI/route.js

import { GoogleGenerativeAI } from "@google/generative-ai";

// POST handler for processing the review and getting response from Gemini
export async function POST(req) {
  try {
    // Extract user input (review text) from the incoming request
    const { userInput } = await req.json();
    console.log("Received user input:", userInput);
    const prompt =
      "Given a paragraph, return average package in integer format, it may be in LPA or lakh or thousand or any other format, return proper number (return zero if found none)" +
      userInput;
    // Ensure the input exists and is between the acceptable character length
    if (!userInput || userInput.length < 200 || userInput.length > 500) {
      return Response.json(
        { error: "Review must be between 200-500 characters" },
        { status: 400 }
      );
    }

    // Use the Gemini API Key (stored securely in environment variables)
    const GEMINI_API_KEY = "AIzaSyDJAvS5grnneKNrpSkIG3qPDN_sJtDUi0g";
    if (!GEMINI_API_KEY) {
      return Response.json({ error: "API key missing" }, { status: 500 });
    }

    // Initialize the GoogleGenerativeAI instance
    const genAI = new GoogleGenerativeAI(GEMINI_API_KEY);
    const model = genAI.getGenerativeModel({ model: "gemini-2.0-flash-exp" });

    // Create a new chat session with specified generation configurations
    const chatSession = model.startChat({
      generationConfig: {
        temperature: 1,
        topP: 0.95,
        topK: 40,
        maxOutputTokens: 8192,
        responseMimeType: "text/plain",
      },
      history: [],
    });

    // Send the user input (review text) to Gemini and retrieve the response
    const result = await chatSession.sendMessage(prompt);
    const responseText = result.response.text();

    // Return the generated response as JSON
    return Response.json({ response: responseText });
  } catch (error) {
    // Log and handle any errors
    console.error("Error calling Gemini API:", error);
    return Response.json(
      { error: "Failed to get response from Gemini" },
      { status: 500 }
    );
  }
}
