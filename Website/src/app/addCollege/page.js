"use client";

import { useState } from "react";
import { auth, db } from "../firebase/firebase";
import { useRouter } from "next/navigation";
import { doc, setDoc, collection } from "firebase/firestore";

export default function AddCollege() {
  const [collegeData, setCollegeData] = useState({
    name: "",
    city: "",
    contact: "",
    desc: "",
    review_count: 0,
  });
  const router = useRouter();

  const handleAddCollege = async (e) => {
    e.preventDefault();

    try {
      // Add college to Firestore
      await setDoc(doc(collection(db, "college"), collegeData.name), {
        name: collegeData.name,
        city: collegeData.city,
        contact: collegeData.contact,
        desc: collegeData.desc,
        review_count: collegeData.review_count,
      });

      // Reset the form
      setCollegeData({
        name: "",
        city: "",
        contact: "",
        desc: "",
        review_count: 0,
      });

      router.push("/dashboard"); // Redirect back to the dashboard after submission
    } catch (error) {
      console.error("Error adding college: ", error);
    }
  };

  return (
    <div className="min-h-screen bg-[#1f2833] text-white flex flex-col items-center justify-center px-4">
      <div className="bg-[#0b171f] p-8 rounded-lg shadow-xl w-full max-w-lg border border-gray-600">
        <h1 className="text-3xl font-bold text-green-400 mb-6 text-center">
          ➕ Add a College
        </h1>

        <form onSubmit={handleAddCollege} className="space-y-6">
          <div>
            <label className="block text-lg font-medium text-gray-400 mb-2">
              College Name
            </label>
            <input
              type="text"
              value={collegeData.name}
              onChange={(e) =>
                setCollegeData({ ...collegeData, name: e.target.value })
              }
              required
              className="w-full px-4 py-3 bg-gray-800 text-white border border-gray-600 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500"
            />
          </div>
          <div>
            <label className="block text-lg font-medium text-gray-400 mb-2">
              City
            </label>
            <input
              type="text"
              value={collegeData.city}
              onChange={(e) =>
                setCollegeData({ ...collegeData, city: e.target.value })
              }
              required
              className="w-full px-4 py-3 bg-gray-800 text-white border border-gray-600 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500"
            />
          </div>
          <div>
            <label className="block text-lg font-medium text-gray-400 mb-2">
              Contact
            </label>
            <input
              type="text"
              value={collegeData.contact}
              onChange={(e) =>
                setCollegeData({ ...collegeData, contact: e.target.value })
              }
              required
              className="w-full px-4 py-3 bg-gray-800 text-white border border-gray-600 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500"
            />
          </div>
          <div>
            <label className="block text-lg font-medium text-gray-400 mb-2">
              Description
            </label>
            <textarea
              value={collegeData.desc}
              onChange={(e) =>
                setCollegeData({ ...collegeData, desc: e.target.value })
              }
              required
              className="w-full px-4 py-3 bg-gray-800 text-white border border-gray-600 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500 h-28"
            />
          </div>
          <div>
            <label className="block text-lg font-medium text-gray-400 mb-2">
              Review Count
            </label>
            <input
              type="number"
              value={collegeData.review_count}
              disabled
              className="w-full px-4 py-3 bg-gray-700 text-gray-300 border border-gray-600 rounded-lg"
            />
          </div>

          <button
            type="submit"
            className="w-full py-3 bg-green-600 text-white rounded-lg text-lg font-semibold shadow-md hover:bg-green-700 focus:outline-none focus:ring-4 focus:ring-green-500 transition ease-in-out duration-300"
          >
            ✅ Add College
          </button>
        </form>
      </div>
    </div>
  );
}
