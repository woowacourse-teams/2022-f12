import styled from "styled-components";

const ButtonLayout = styled.div`
  width: max-content;
  margin: 20px;
  padding: 10px;
  background-color: #f6bebe;
  border-radius: 10px;
  border: none;
  box-shadow: 2px 2px 2px gray;
  color: black;
  &:active {
    position: relative;
    top: 2px;
    left: 2px;
    box-shadow: none;
  }
`;

const Button = ({ onClick, text }) => {
  return <ButtonLayout onClick={onClick}>{text}</ButtonLayout>;
};

export default Button;
