// src/app/profile/page.js
"use client"; // Ensure this is a client component

import { useEffect, useState } from "react";
import { auth, db } from "../firebase/firebase";
import { useRouter } from "next/navigation"; // Updated import
import { onAuthStateChanged } from "firebase/auth";
import { doc, getDoc } from "firebase/firestore";

export default function Profile() {
  const [userData, setUserData] = useState(null);
  const [loading, setLoading] = useState(true);
  const router = useRouter(); // Using the correct import

  useEffect(() => {
    const unsubscribe = onAuthStateChanged(auth, async (user) => {
      if (user) {
        const docRef = doc(db, "nikhil", user.email);
        const docSnap = await getDoc(docRef);

        if (docSnap.exists()) {
          setUserData(docSnap.data());
        } else {
          console.log("No such document!");
        }
        setLoading(false);
      } else {
        router.push("/login");
      }
    });

    return () => unsubscribe();
  }, [router]);

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen bg-[#1f2833] text-white">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-green-400"></div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-[#1f2833] flex flex-col items-center justify-center text-white px-4">
      <div className="bg-[#0b171f] p-8 rounded-lg shadow-xl w-full max-w-md border border-gray-600 text-center">
        <h1 className="text-3xl font-bold text-green-400 mb-4">Profile</h1>
        {userData ? (
          <div className="space-y-4">
            <p className="text-lg">
              <strong className="text-gray-400">Name:</strong> {userData.name}
            </p>
            <p className="text-lg">
              <strong className="text-gray-400">Email:</strong> {userData.email}
            </p>
            <p className="text-lg">
              <strong className="text-gray-400">Institute:</strong>{" "}
              {userData.institute}
            </p>
          </div>
        ) : (
          <p className="text-red-400">No user data found</p>
        )}
      </div>
    </div>
  );
}
