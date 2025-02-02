"use client"; // Ensure this is a client component

import { useEffect, useState } from "react";
import { auth, db } from "../firebase/firebase";
import { useRouter } from "next/navigation";
import { collection, getDocs } from "firebase/firestore";
import { onAuthStateChanged, signOut } from "firebase/auth";
import Link from "next/link";

export default function Dashboard() {
  const [user, setUser] = useState(null);
  const [colleges, setColleges] = useState([]);
  const router = useRouter();

  useEffect(() => {
    const unsubscribe = onAuthStateChanged(auth, (user) => {
      if (user) {
        setUser(user);
        fetchColleges(); // Fetch colleges once user is set
      } else {
        router.push("/login");
      }
    });

    return () => unsubscribe();
  }, [router]);

  const handleLogout = async () => {
    await signOut(auth);
    router.push("/login");
  };

  // Fetch colleges from Firestore
  const fetchColleges = async () => {
    try {
      const querySnapshot = await getDocs(collection(db, "college"));
      const collegeList = querySnapshot.docs.map((doc) => ({
        id: doc.id,
        name: doc.data().name || "Unknown College",
      }));

      setColleges(collegeList);
    } catch (error) {
      console.error("Error fetching colleges:", error);
    }
  };

  return (
    <div className="min-h-screen bg-[#1f2833] text-white flex flex-col items-center p-8">
      {user ? (
        <>
          {/* Header */}
          <div className="w-full flex justify-between items-center mb-6 max-w-4xl">
            <h1 className="text-2xl font-bold">Welcome, {user.email}</h1>
            <div className="flex space-x-4">
              <button
                onClick={() => router.push("/profile")}
                className="bg-green-500 text-white px-4 py-2 rounded-lg shadow-md hover:bg-green-600 transition duration-300"
              >
                Profile
              </button>
              <button
                onClick={handleLogout}
                className="bg-red-500 text-white px-4 py-2 rounded-lg shadow-md hover:bg-red-600 transition duration-300"
              >
                Logout
              </button>
            </div>
          </div>

          {/* College List */}
          <div className="w-full max-w-4xl bg-[#0b171f] shadow-lg rounded-lg p-6 border border-gray-600">
            <h2 className="text-xl font-semibold mb-4">Colleges List</h2>
            {colleges.length > 0 ? (
              <ul className="space-y-4">
                {colleges.map((college) => (
                  <li
                    key={college.id}
                    className="cursor-pointer border-b border-gray-700 pb-4 last:border-none hover:shadow-md transition-shadow duration-300"
                  >
                    <Link href={`/mycollege/${college.id}`}>
                      <h3 className="text-lg font-semibold hover:text-green-400 transition duration-300">
                        {college.name}
                      </h3>
                    </Link>
                  </li>
                ))}
              </ul>
            ) : (
              <p className="text-gray-400">No colleges available.</p>
            )}
          </div>

          {/* Add College Button (Only for admin user) */}
          {user.email === "hack@gmail.com" && (
            <button
              onClick={() => router.push("/addCollege")}
              className="fixed bottom-10 right-10 bg-green-500 text-white px-6 py-3 rounded-full shadow-lg hover:bg-green-600 transition duration-300"
            >
              Add College
            </button>
          )}
        </>
      ) : (
        <p className="text-gray-400">Loading...</p>
      )}
    </div>
  );
}
