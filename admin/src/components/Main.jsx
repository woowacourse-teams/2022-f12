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

const Navigator = styled.section`
  display: flex;
  justify-content: center;
`;

const Main = ({ accessToken }) => {
  const [products, setProducts] = useState();
  const [searchInput, setSearchInput] = useState();
  const [pageNumber, setPageNumber] = useState(0);
  const [hasNext, setHasNext] = useState(false);
  const navigate = useNavigate();
  const size = 10;

  const getProducts = async () => {
    try {
      const response = await axios.get(`${API_BASE_URL}/products`, {
        params: { page: pageNumber, size, query: searchInput },
      });
      setProducts(response.data.items);
      setHasNext(response.data.hasNext);
    } catch (err) {
      alert(`${response.status} error: ${response.data.message}`);
    }
  };

  useEffect(() => {
    getProducts();
  }, [searchInput, pageNumber]);

  return (
    <>
      <HeaderLayOut />
      <Contents>
        <Button
          onClick={() => {
            navigate("/insertProduct");
          }}
        >
          제품 추가
        </Button>
        <SearchBar searchInput={searchInput} setSearchInput={setSearchInput} />
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
      <Navigator>
        {pageNumber === 0 ? null : (
          <Button
            onClick={() => {
              setPageNumber(pageNumber - 1);
            }}
          >
            이전 페이지
          </Button>
        )}
        {hasNext === false ? null : (
          <Button
            onClick={() => {
              setPageNumber(pageNumber + 1);
            }}
          >
            다음 페이지
          </Button>
        )}
      </Navigator>
    </>
  );
};

export default Main;
