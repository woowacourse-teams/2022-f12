import { useState } from "react";
import { Route, Routes } from "react-router-dom";
import InsertProduct from "./components/InsertProduct";
import Landing from "./components/Landing";
import Login from "./components/Login";
import Main from "./components/Main";

function App() {
  const initialToken = window.sessionStorage.getItem("accessToken");
  const [accessToken, setAccessToken] = useState(initialToken || "");
  return (
    <Routes>
      <Route path="/" element={<Landing />} />
      <Route
        path="/login"
        element={<Login setAccessToken={setAccessToken} />}
      />
      <Route
        path="/main"
        element={
          <Main accessToken={accessToken} setAccessToken={setAccessToken} />
        }
      />
      <Route
        path="/insertProduct"
        element={<InsertProduct accessToken={accessToken} />}
      />
    </Routes>
  );
}

export default App;
