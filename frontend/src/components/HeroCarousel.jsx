import React from "react";
import "../App.css";

const HeroCarousel = () => {
  return (
    <div className="carousel">
      <div className="carousel-image">
        <button className="arrow left">◀</button>
        Slide Image Here
        <button className="arrow right">▶</button>
      </div>
      <div className="dots">
        <span className="dot"></span>
        <span className="dot"></span>
        <span className="dot"></span>
        <span className="dot"></span>
      </div>
    </div>
  );
};

export default HeroCarousel;
