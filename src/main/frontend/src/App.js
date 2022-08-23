import logo from './logo.svg';
import './App.css';
import React, { useState, useEffect } from 'react';
function App() {
  const [number, setNumber] = useState(0);
  useEffect(() => {
      const fetchData = async () => {
          const res = await fetch('/api/test', {
              headers : {
                  'Content-Type': 'application/json',
                  'Accept': 'application/json'
              }
          })
          const json = await res.json()
          setNumber(json.number)
      }
      fetchData()
  }, [])
  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          Highest number: {number}
        </p>
      </header>
    </div>
  );
}

export default App;
