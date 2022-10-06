import axios from "axios";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import styled from "styled-components";

import HeaderLayOut from "./HeaderLayOut";
import { API_BASE_URL } from "../constants/urls";
import Product from "./Product";
import SearchBar from "./SearchBar";

const Contents = styled.div`
  margin: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
`;

const Button = styled.div`
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

const Main = ({ accessToken }) => {
  const [products, setProducts] = useState();
  const [searchInput, setSearchInput] = useState();
  const navigate = useNavigate();
  const page = 0;
  const size = 10;

  const getProducts = async () => {
    try {
      const response = await axios.get(`${API_BASE_URL}/products`, {
        params: { page, size, query: searchInput },
      });
      setProducts(response.data.items);
    } catch (err) {
      alert(`${response.status} error: ${response.data.message}`);
    }
  };

  useEffect(() => {
    getProducts();
  }, [searchInput]);

  return (
    <>
      <HeaderLayOut />
      <Contents>
        <SearchBar searchInput={searchInput} setSearchInput={setSearchInput} />
        <Button
          onClick={() => {
            navigate("/insertProduct");
          }}
        >
          제품 추가
        </Button>
        {products?.map((product) => {
          return (
            <Product
              key={product.id}
              productData={product}
              accessToken={accessToken}
              handleRefetch={getProducts}
            />
          );
        })}
      </Contents>
    </>
  );
};

export default Main;
