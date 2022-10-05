import { useState } from "react";
import { Route, Routes } from "react-router-dom";
import Landing from "./components/Landing";
import Login from "./components/Login";
import Main from "./components/Main";

function App() {
  const [accessToken, setAccessToken] = useState();
  return (
    <Routes>
      <Route path="/" element={<Landing />} />
      <Route
        path="/login"
        element={<Login setAccessToken={setAccessToken} />}
      />
      <Route path="/main" element={<Main accessToken={accessToken} />} />
    </Routes>
  );
}

export default App;
