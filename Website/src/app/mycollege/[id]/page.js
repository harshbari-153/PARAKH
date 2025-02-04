"use client";
import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { db } from "../../firebase/firebase";
import { doc, getDoc } from "firebase/firestore";

export default function CollegeDetail({ params }) {
  const [college, setCollege] = useState(null);
  const [reviews, setReviews] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [id, setId] = useState(null);
  const router = useRouter();

  useEffect(() => {
    const fetchParams = async () => {
      const resolvedParams = await params;
      setId(resolvedParams.id);
    };
    fetchParams();
  }, [params]);

  useEffect(() => {
    if (!id) return;

    const fetchCollegeData = async () => {
      try {
        const docRef = doc(db, "college", id);
        const docSnap = await getDoc(docRef);

        if (docSnap.exists()) {
          setCollege(docSnap.data());
        }
      } catch (error) {
        setError("Error fetching college data");
      }
    };

    const fetchReviews = async () => {
      try {
        const docRef = doc(db, "reviews", id);
        const docSnap = await getDoc(docRef);

        if (docSnap.exists()) {
          const reviewData = docSnap.data();
          const extractedReviews = Object.values(reviewData).map(
            (reviewObj) => reviewObj.review
          );
          setReviews(extractedReviews);
        }
      } catch (error) {
        setError("Error fetching reviews");
      } finally {
        setLoading(false);
      }
    };

    fetchCollegeData();
    fetchReviews();
  }, [id]);

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen bg-[#1f2833] text-white">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-green-400"></div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-[#1f2833] text-white flex flex-col items-center py-10 px-4">
      <div className="bg-[#0b171f] p-8 rounded-lg shadow-xl w-full max-w-lg border border-gray-600">
        <h1 className="text-3xl font-bold text-green-400 mb-4 text-center">
          College Details
        </h1>

        {college ? (
          <div className="space-y-4">
            <p className="text-lg">
              <strong className="text-gray-400">🏫 Name:</strong> {college.name}
            </p>
            <p className="text-lg">
              <strong className="text-gray-400">📍 City:</strong> {college.city}
            </p>
            <p className="text-lg">
              <strong className="text-gray-400">📞 Contact:</strong>{" "}
              {college.contact}
            </p>
            <p className="text-lg">
              <strong className="text-gray-400">📄 Description:</strong>{" "}
              {college.desc}
            </p>
            <p className="text-lg">
              <strong className="text-gray-400">⭐ Review Count:</strong>{" "}
              {college.review_count}
            </p>
          </div>
        ) : (
          <p className="text-red-400 text-center">No college data found.</p>
        )}
      </div>

      {/* Reviews Section */}
      <div className="bg-[#0b171f] p-6 mt-6 rounded-lg shadow-md w-full max-w-lg border border-gray-600">
        <h2 className="text-2xl font-semibold text-green-400 mb-4 text-center">
          Reviews
        </h2>

        {reviews.length > 0 ? (
          <ul className="space-y-4">
            {reviews.map((review, index) => (
              <li
                key={index}
                className="bg-gray-800 p-4 rounded-lg border border-gray-700 text-gray-300 hover:bg-gray-700 transition duration-300"
              >
                {review}
              </li>
            ))}
          </ul>
        ) : (
          <p className="text-gray-400 text-center">
            No reviews available for this college.
          </p>
        )}
      </div>

      {/* Add Review Button */}
      <button
        onClick={() => router.push(`/addReview?id=${id}`)}
        className="mt-6 bg-blue-600 hover:bg-blue-700 text-white px-6 py-3 rounded-lg text-lg font-semibold shadow-md transition-all duration-300 ease-in-out transform hover:scale-105"
      >
        ➕ Add Review
      </button>
    </div>
  );
}
