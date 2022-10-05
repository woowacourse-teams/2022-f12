import axios from "axios";
import { useEffect, useState } from "react";
import styled from "styled-components";

import HeaderLogoImage from "../assets/HeaderLogo.svg";
import { API_BASE_URL } from "../constants/urls";
import Product from "./Product";
import Items from "./Product";

const HeaderLayOut = styled.div`
  width: 100vw;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
`;

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
`;

const Main = ({ accessToken }) => {
  const [products, setProducts] = useState();
  const page = 0;
  const size = 10;

  const getProducts = async () => {
    const response = await axios.get(API_BASE_URL + "/products", {
      params: { page, size },
    });
    console.log(response.data.items);
    setProducts(response.data.items);
  };
  useEffect(() => {
    getProducts();
  }, []);
  return (
    <HeaderLayOut>
      <HeaderLogoImage />
      {products?.map((product) => {
        return (
          <Product
            key={product.id}
            productData={product}
            accessToken={accessToken}
          />
        );
      })}
    </HeaderLayOut>
  );
};

export default Main;
