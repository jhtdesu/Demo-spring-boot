import React from "react";
import "../App.css";

const services = [
  { title: "Cô chủ nhà xinh đẹp và anh hàng xóm may mắn", image: "https://imgur.com/e8YgL0L.jpg" },
  { title: "Chuyển sinh thành tứ đại dâm", image: "https://via.placeholder.com/150" },
  { title: "Chuyển sinh thành con chó của cô chủ xinh đẹp", image: "https://via.placeholder.com/150" },
  { title: "Ta sẽ làm bá vương harem!!", image: "https://via.placeholder.com/150" },
];

const Sidebar = () => {
  return (
    <aside className="sidebar">
      <div className="sidebar-logo">iHentai</div>
      <ul className="sidebar-menu">
        <li>Trang chủ</li>
        <li>Khám phá</li>
        <li>Top lượt xem</li>
        <li>Hentai Vietsub</li>
        <li>Hentai 3D</li>
        <li>Không che</li>
        <li>Phim đã xem</li>
        <li>Phim đã lưu</li>
        <li>Bộ sưu tập</li>
      </ul>
    </aside>
  );
};

const ServiceList = () => {
  return (
    <div className="layout">
      <Sidebar />
      <main className="main-content">
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
      </main>
    </div>
  );
};

export default ServiceList;
