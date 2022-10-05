import styled from 'styled-components';

import HeaderLogoImage from './assets/HeaderLogo.svg';

const HeaderLayOut = styled.div`
  width: 100vw;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
`

const LoginButton = styled.button`
  margin: 20px;
  padding: 10px;
  font-size: 20px;
  background-color: #f6bebe;
  border-radius: 10px;
  border: none;
  box-shadow: 2px 2px 2px gray;
  cursor: pointer;
  &:active {
    position: relative;
    top: 2px;
    left: 2px;
    box-shadow: none;
  }
`

const onClick = e => {
  alert("로그인!");
  location.href = "/";
};

function App() {
  return (
    <HeaderLayOut>
      <HeaderLogoImage />
      <LoginButton onClick={onClick}>
        GitHub로 로그인
      </LoginButton>
    </HeaderLayOut>
  );
}

export default App;
