import { useState } from "react";
import { Route, Routes } from "react-router-dom";
import InsertProduct from "./components/InsertProduct";
import Landing from "./components/Landing";
import Login from "./components/Login";
import Main from "./components/Main";
import NotFound from "./components/NotFound";

function App() {
  const [accessToken, setAccessToken] = useState();
  return (
    <Routes>
      <Route path="/" element={<Landing />} />
      <Route
        path="/login"
        element={<Login setAccessToken={setAccessToken} />}
      />
      {accessToken && (
        <>
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
        </>
      )}
      <Route path="*" element={<NotFound />} />
    </Routes>
  );
}

export default App;
