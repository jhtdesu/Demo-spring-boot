import React, { useState } from "react";
import "../App.css";
import Navbar from "./Navbar";
import HeroCarousel from "./HeroCarousel";
import SideBar from "./SideBar";

const services = [
  { title: "Cô chủ nhà xinh đẹp và anh hàng xóm may mắn", image: "https://imgur.com/e8YgL0L.jpg" },
  { title: "Chuyển sinh thành tứ đại dâm", image: "https://via.placeholder.com/150" },
  { title: "Chuyển sinh thành con chó của cô chủ xinh đẹp", image: "https://via.placeholder.com/150" },
  { title: "Ta sẽ làm bá vương harem!!", image: "https://via.placeholder.com/150" },
];

const Layout = () => {
  const [sidebarOpen, setSidebarOpen] = useState(false);

  const openSidebar = () => setSidebarOpen(true);
  const closeSidebar = () => setSidebarOpen(false);

  return (
    <div className="app-container">
      <Navbar sidebarOpen={sidebarOpen} openSidebar={openSidebar} closeSidebar={closeSidebar} />
      <SideBar sidebarOpen={sidebarOpen} closeSidebar={closeSidebar} />
      <div id="main" className={sidebarOpen ? "shifted" : ""}>
        <HeroCarousel />
        <section className="services">
          <h2 className="services-title">Featured Services</h2>
          <div className="service-grid">
            {services.map((service, index) => (
              <div key={index} className="service-card">
                <img
                  src={service.image}
                  alt={service.title}
                  className="service-image"
                  style={{ width: "100%", height: "150px", objectFit: "cover" }}
                />
                <div className="service-title">{service.title}</div>
                <button className="view-btn">View</button>
              </div>
            ))}
          </div>
        </section>
      </div>
    </div>
  );
};

export default Layout;