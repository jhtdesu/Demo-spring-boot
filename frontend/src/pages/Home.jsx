import React from "react";
import Navbar from "../components/Navbar";
import HeroCarousel from "../components/HeroCarousel";
import ServiceList from "../components/ServiceList.jsx";

const Home = () => {
  return (
    <div className="min-h-screen bg-white text-gray-800">
      <Navbar />
      <HeroCarousel />
      <ServiceList />
    </div>
  );
};

export default Home;