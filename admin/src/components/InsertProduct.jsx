import axios from "axios";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import styled from "styled-components";
import { API_BASE_URL } from "../constants/urls";
import Button from "./Button";
import ProductCategorySelect from "./ProductCategorySelect";

const Contents = styled.section`
  margin: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
`;

const InsertProduct = ({ accessToken }) => {
  const [productName, setProductName] = useState();
  const [productCategory, setProductCategory] = useState("keyboard");
  const [productImageUrl, setProdcutImageURL] = useState();
  const navigate = useNavigate();

  const requestInsertProduct = async () => {
    try {
      await axios.post(
        `${API_BASE_URL}/products`,
        {
          name: productName,
          category: productCategory,
          imageUrl: productImageUrl,
        },
        { headers: { Authorization: `Bearer ${accessToken}` } }
      );
      navigate("/main");
    } catch (err) {
      const response = err.response;
      alert(`${response.status} error: ${response.data.message}`);
    }
  };
  return (
    <>
      <div>
        <label htmlFor="">제품명</label>
        <input
          onChange={(e) => {
            setProductName(e.target.value);
          }}
          type="text"
        />
      </div>
      <div>
        <label htmlFor="">카테고리</label>
        <ProductCategorySelect
          categoryInputValue={productCategory}
          setCategoryInputValue={setProductCategory}
        />
      </div>
      <div>
        <label htmlFor="">이미지 주소</label>
        <input
          onChange={(e) => {
            setProdcutImageURL(e.target.value);
          }}
          type="text"
        />
      </div>
      <Button onClick={requestInsertProduct} text="제출"></Button>
    </>
  );
};

export default InsertProduct;
