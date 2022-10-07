import { Outlet } from "react-router-dom";
import styled from "styled-components";
import HeaderLayOut from "./HeaderLayOut";

const Contents = styled.section`
  margin: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
`;

const PageLayout = () => {
  return (
    <>
      <HeaderLayOut />
      <Contents>
        <Outlet />
      </Contents>
    </>
  );
};

export default PageLayout;
