import axios from "axios";
import { useEffect } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import { API_BASE_URL } from "../constants/urls";

const Login = ({ setAccessToken }) => {
  const [params] = useSearchParams();
  const navigate = useNavigate();
  const code = params.get("code");
  const sendLoginRequest = async () => {
    try {
      const response = await axios.get(`${API_BASE_URL}/login/admin`, {
        params: { code },
      });
      setAccessToken(response.data.token);
    } catch (err) {
      alert(`${response.status} error: ${response.data.message}`);
      navigate("/");
    }
  };
  useEffect(() => {
    sendLoginRequest().then(() => {
      navigate("/main");
    });
  }, []);

  return <div>로그인 중입니다....</div>;
};

export default Login;
