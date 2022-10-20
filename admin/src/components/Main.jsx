import axios from "axios";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import styled from "styled-components";

import HeaderLayOut from "./HeaderLayOut";
import { API_BASE_URL } from "../constants/urls";
import Product from "./Product";
import SearchBar from "./SearchBar";
import Button from "./Button";

const Contents = styled.section`
  margin: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
`;

const Navigator = styled.section`
  display: flex;
  justify-content: center;
`;

const Main = ({ accessToken, setAccessToken }) => {
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
      <Navigator>
        <Button
          onClick={() => {
            navigate("/insertProduct");
          }}
          text="제품 추가"
        />
        <Button
          onClick={() => {
            setAccessToken();
            navigate("/");
          }}
          text="로그아웃"
        />
      </Navigator>
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
      <Navigator>
        {pageNumber === 0 ? null : (
          <Button
            onClick={() => {
              setPageNumber(pageNumber - 1);
            }}
            text="이전 페이지"
          />
        )}
        {hasNext === false ? null : (
          <Button
            onClick={() => {
              setPageNumber(pageNumber + 1);
            }}
            text="다음 페이지"
          />
        )}
      </Navigator>
    </>
  );
};

export default Main;
