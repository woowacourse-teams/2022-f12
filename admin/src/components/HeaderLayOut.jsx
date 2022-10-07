import styled from "styled-components";
import HeaderLogoImage from "../assets/HeaderLogo.svg";

const Layout = styled.div`
  width: 100%;
  padding-top: 10px;
  padding-bottom: 10px;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
`;

const HeaderLayOut = () => {
  return (
    <Layout>
      <HeaderLogoImage />
    </Layout>
  );
};

export default HeaderLayOut;
