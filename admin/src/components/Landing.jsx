import styled from "styled-components";

import { GITHUB_AUTH_URL } from "../constants/urls.js";

const LoginButton = styled.a`
  margin: 20px;
  padding: 10px;
  font-size: 20px;
  background-color: #f6bebe;
  border-radius: 10px;
  border: none;
  box-shadow: 2px 2px 2px gray;
  text-decoration: none;
  color: black;
  &:visited {
    color: black;
  }
  &:active {
    position: relative;
    top: 2px;
    left: 2px;
    box-shadow: none;
  }
`;

const Landing = () => {
  return <LoginButton href={GITHUB_AUTH_URL}>GitHub로 로그인</LoginButton>;
};

export default Landing;
