import { initializeApp, getApps, getApp } from "firebase/app";
import { getAuth } from "firebase/auth";
import { getFirestore } from "firebase/firestore";
const firebaseConfig = {
  apiKey: "AIzaSyAKRTPrOZWEAiGProe4Xf3U0YjbxhHkb8k",
  authDomain: "parakh-eab01.firebaseapp.com",
  projectId: "parakh-eab01",
  storageBucket: "parakh-eab01.firebasestorage.app",
  messagingSenderId: "212239757664",
  appId: "1:212239757664:web:ae5f7b824221ce80aeae05",
};

// Prevent Firebase from initializing multiple times
const app = !getApps().length ? initializeApp(firebaseConfig) : getApp();

export const auth = getAuth(app);
export const db = getFirestore(app);
