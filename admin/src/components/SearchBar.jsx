import { useState } from "react";
import styled from "styled-components";

import SearchImage from "../assets/search.svg";

const Container = styled.div`
  margin: 20px;
  width: 60%;
  display: flex;
  align-items: center;
`;

const Input = styled.input`
  width: 100%;
  height: 2.8rem;
  background: #ffffff;
  outline: none;
  border: 1px solid #f6bebe;
  border-radius: 1.625rem;
  padding: 0 3.5rem 0 1.5rem;
  font-size: 1rem;
  &:focus {
    border: 2px solid #f6bebe;
    transition: 0.3s linear;
  }
`;

const Button = styled.button`
  width: 3.5rem;
  height: 2.8rem;
  margin-left: -3.5rem;
  background: none;
  border: none;
  outline: none;
  cursor: pointer;
`;

const SearchBar = ({ searchInput, setSearchInput }) => {
  const [tempSearchInput, setTempSearchInput] = useState(searchInput);

  const handleInputChange = (e) => {
    setTempSearchInput(e.target.value);
  };

  const setState = () => {
    setSearchInput(tempSearchInput);
  };

  const handleKeyDown = (e) => {
    if (e.key === "Enter") {
      setState();
    }
  };

  return (
    <Container>
      <Input
        maxLength={100}
        type="text"
        value={tempSearchInput || ""}
        onChange={handleInputChange}
        onKeyDown={handleKeyDown}
      />
      <Button onClick={setState}>
        <SearchImage />
      </Button>
    </Container>
  );
};

export default SearchBar;
