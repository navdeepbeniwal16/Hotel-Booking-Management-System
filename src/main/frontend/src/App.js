import "./App.css";
// import React, { useState, useEffect } from "react";

import "bootstrap/dist/css/bootstrap.min.css";
import { MainNavbar } from "./components/layouts/MainNavbar";

import { Home } from "./pages/Home";
import { Bookings } from "./pages/Bookings";
import { Route, Routes } from "react-router-dom";
import { Container } from "react-bootstrap";

function App() {
  // const [number, setNumber] = useState(0);
  // useEffect(() => {
  //   const fetchData = async () => {
  //     const res = await fetch("/api/test", {
  //       headers: {
  //         "Content-Type": "application/json",
  //         Accept: "application/json",
  //       },
  //     });
  //     const json = await res.json();
  //     setNumber(json.number);
  //   };
  //   fetchData();
  // }, []);
  return (
    <div>
      <header>
        <MainNavbar></MainNavbar>
      </header>
      <Container>
        <Routes>
          <Route path="/" element={<Home></Home>}></Route>
          <Route path="/bookings" element={<Bookings></Bookings>}></Route>
        </Routes>
      </Container>
    </div>
  );
}

export default App;
