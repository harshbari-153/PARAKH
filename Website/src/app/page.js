"use client";
import Link from "next/link";

export default function LandingPage() {
  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-[#1f2833] text-white px-4">
      <h1 className="text-5xl font-bold mb-6 text-center">Welcome to PARAKH</h1>
      <p className="text-lg max-w-xl text-center mb-8 text-gray-300">
        Get clarity on college reviews with AI assistance. Discover honest
        opinions and make informed decisions.
      </p>

      <div className="flex space-x-6">
        <Link href="/login">
          <button className="bg-blue-500 hover:bg-blue-600 text-white px-8 py-4 rounded-lg text-xl font-semibold shadow-lg transform transition-all duration-300 ease-in-out hover:scale-105">
            Login
          </button>
        </Link>
        <Link href="/register">
          <button className="bg-green-500 hover:bg-green-600 text-white px-8 py-4 rounded-lg text-xl font-semibold shadow-lg transform transition-all duration-300 ease-in-out hover:scale-105">
            Register
          </button>
        </Link>
      </div>
    </div>
  );
}
