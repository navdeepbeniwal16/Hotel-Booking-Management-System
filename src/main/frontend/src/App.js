import "./App.css";
// import React, { useState, useEffect } from "react";

import { Button } from "react-bootstrap";
import "bootstrap/dist/css/bootstrap.min.css";
import { MainNavbar } from "./components/layouts/MainNavbar";

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
    </div>
  );
}

export default App;
