import styled from 'styled-components';

import { GITHUB_AUTH_URL } from '../constants/urls.js';
import HeaderLogoImage from '../assets/HeaderLogo.svg';

const HeaderLayOut = styled.div`
  width: 100vw;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
`

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
`

const Landing = () => {
  return (
    <HeaderLayOut>
      <HeaderLogoImage />
      <LoginButton href={GITHUB_AUTH_URL}>
        GitHub로 로그인
      </LoginButton>
    </HeaderLayOut>
  );
}

export default Landing;
