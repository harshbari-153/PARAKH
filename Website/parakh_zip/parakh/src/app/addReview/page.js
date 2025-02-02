"use client";

import { useSearchParams } from "next/navigation";
import { useState } from "react";
import axios from "axios";

export default function AddReview() {
  const searchParams = useSearchParams();
  const id = searchParams.get("id"); // Get the college ID from query params

  const [review, setReview] = useState("");
  const [error, setError] = useState(""); // Error message for validation
  const [loading, setLoading] = useState(false);
  const [geminiResponse, setGeminiResponse] = useState("");

  // Handle submitting review to Gemini API
  const handleGeminiRequest = async () => {
    setLoading(true);
    setError(""); // Clear previous error

    try {
      if (review.length < 200 || review.length > 500) {
        setError("Review must be between 200-500 characters.");
        return;
      }

      // Send the review to the Gemini API
      const response = await axios.post("/api/gemini", { userInput: review });

      // Handle the response from Gemini API
      if (response.data.response) {
        setGeminiResponse(response.data.response);
      } else {
        setGeminiResponse("No response received.");
      }
    } catch (error) {
      console.error("Error fetching Gemini response:", error);
      setGeminiResponse("Failed to get response from AI.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100">
      <h1 className="text-3xl font-bold mb-4">Add Review</h1>
      <div className="bg-white p-6 rounded-lg shadow-md w-96 text-center">
        {/* Review Input */}
        <form className="mb-4">
          <textarea
            value={review}
            onChange={(e) => setReview(e.target.value)}
            placeholder="Write your review here (200-500 characters)..."
            rows={4}
            maxLength={500}
            className="w-full p-2 border rounded mb-2"
          />
          <p
            style={{
              fontSize: "12px",
              color: review.length < 200 ? "red" : "green",
            }}
          >
            {review.length}/500 characters
          </p>

          {error && <p className="text-red-500">{error}</p>}

          <button
            type="button"
            onClick={handleGeminiRequest}
            className="bg-purple-500 text-white p-2 rounded w-full"
          >
            {loading ? "Generating..." : "Generating Score"}
          </button>
        </form>

        {/* Display Gemini AI Response */}
        {geminiResponse && (
          <div className="mt-4 bg-gray-200 p-3 rounded">
            <h3 className="font-semibold">AI Analysis:</h3>
            <p>{geminiResponse}</p>
          </div>
        )}
      </div>
    </div>
  );
}
