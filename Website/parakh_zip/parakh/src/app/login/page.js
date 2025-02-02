"use client";
import { useState } from "react";
import { useRouter } from "next/navigation";
import { auth } from "@/app/firebase/firebase";
import { signInWithEmailAndPassword } from "firebase/auth";
import Link from "next/link";

export default function LoginPage() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const router = useRouter();

  const handleLogin = async (e) => {
    e.preventDefault();
    setError("");

    try {
      await signInWithEmailAndPassword(auth, email, password);
      router.push("/dashboard"); // Redirect to dashboard after successful login
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-[#1f2833] text-white px-4">
      <div className="bg-[#0b171f] p-8 rounded-lg shadow-xl w-full max-w-md border border-gray-600">
        <h2 className="text-3xl font-semibold text-center text-gray-200 mb-6">
          Login
        </h2>

        {error && <p className="text-red-500 text-center mb-4">{error}</p>}

        <form onSubmit={handleLogin} className="space-y-6">
          <div>
            <input
              type="email"
              placeholder="Email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
              className="w-full px-4 py-3 bg-gray-800 text-white border border-gray-600 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>
          <div>
            <input
              type="password"
              placeholder="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              className="w-full px-4 py-3 bg-gray-800 text-white border border-gray-600 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>
          <button
            type="submit"
            className="w-full py-3 bg-blue-600 text-white rounded-lg text-lg font-semibold shadow-md hover:bg-blue-700 focus:outline-none focus:ring-4 focus:ring-blue-500 transition ease-in-out duration-300"
          >
            Login
          </button>
        </form>

        <p className="mt-4 text-center text-gray-400">
          Don't have an account?{" "}
          <Link
            href="/register"
            className="text-blue-400 hover:text-blue-500 font-semibold"
          >
            Register
          </Link>
        </p>
      </div>
    </div>
  );
}
