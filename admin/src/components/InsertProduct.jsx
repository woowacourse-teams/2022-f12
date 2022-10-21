import axios from "axios";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import styled from "styled-components";
import { API_BASE_URL } from "../constants/urls";
import Button from "./Button";
import ProductCategorySelect from "./ProductCategorySelect";

const InsertProduct = ({ accessToken }) => {
  const [productName, setProductName] = useState();
  const [productCategory, setProductCategory] = useState("keyboard");
  const [productImageUrl, setProductImageURL] = useState();
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
      <Container>
        <FieldContainer>
          <label htmlFor="">제품명</label>
          <CustomInput
            onChange={(e) => {
              setProductName(e.target.value);
            }}
            type="text"
          />
        </FieldContainer>
        <FieldContainer>
          <label htmlFor="">카테고리</label>
          <ProductCategorySelect
            categoryInputValue={productCategory}
            setProductCategory={setProductCategory}
          />
        </FieldContainer>
        <FieldContainer>
          <label htmlFor="">이미지 주소</label>
          <CustomInput
            onChange={(e) => {
              setProductImageURL(e.target.value);
            }}
            type="text"
          />
        </FieldContainer>
        <Button onClick={requestInsertProduct}>제출</Button>
      </Container>
      <h1>이미지 프리뷰</h1>
      <PreviewImage src={productImageUrl} />
    </>
  );
};

export default InsertProduct;

const Container = styled.section`
  display: flex;
  gap: 1rem;
  align-items: center;
  height: 1.5rem;
`;

const FieldContainer = styled.div`
  display: flex;
  gap: 1rem;
  align-items: center;
  height: 100%;
`;

const CustomInput = styled.input`
  height: 100%;
`;

const PreviewImage = styled.img`
  border: 1px solid black;
`;
