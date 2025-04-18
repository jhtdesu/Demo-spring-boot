import React from "react";
import "../App.css";

const Sidebar = ({ collapsed, toggleSidebar }) => {
  return (
    <aside className={`sidebar ${collapsed ? "collapsed" : ""}`}>
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
      <button className="toggle-button" onClick={toggleSidebar}>
        {collapsed ? ">" : "<"}
      </button>
    </aside>
  );
};

export default Sidebar;
