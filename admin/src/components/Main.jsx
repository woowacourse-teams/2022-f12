import axios from "axios";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import styled from "styled-components";

import HeaderLogoImage from "../assets/HeaderLogo.svg";
import { API_BASE_URL } from "../constants/urls";
import Product from "./Product";

const HeaderLayOut = styled.div`
  width: 100vw;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
`;

const Main = ({ accessToken }) => {
  const [products, setProducts] = useState();
  const navigate = useNavigate();
  const page = 0;
  const size = 10;

  const getProducts = async () => {
    try {
      const response = await axios.get(`${API_BASE_URL}/products`, {
        params: { page, size },
      });
      setProducts(response.data.items);
    } catch (err) {
      alert(`${response.status} error: ${response.data.message}`);
    }
  };

  useEffect(() => {
    getProducts();
  }, []);

  return (
    <HeaderLayOut>
      <HeaderLogoImage />
      <button
        onClick={() => {
          navigate("/insertProduct");
        }}
      >
        제품 추가
      </button>
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
    </HeaderLayOut>
  );
};

export default Main;
