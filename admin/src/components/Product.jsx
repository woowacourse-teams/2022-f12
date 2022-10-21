import axios from "axios";
import { useState } from "react";
import styled from "styled-components";
import { API_BASE_URL } from "../constants/urls";
import Button from "./Button";
import ProductCategorySelect from "./ProductCategorySelect";

const Product = ({ productData, accessToken, handleRefetch }) => {
  const [editMode, setEditMode] = useState(false);
  const [productName, setProductName] = useState(productData.name);
  const [productCategory, setProductCategory] = useState(productData.category);
  const [productImageUrl, setProductImageURL] = useState(productData.imageUrl);

  const requestUpdate = async (id) => {
    try {
      await axios.patch(
        `${API_BASE_URL}/products/${id}`,
        {
          name: productName,
          imageUrl: productImageUrl,
          category: productCategory,
        },
        { headers: { Authorization: "Bearer " + accessToken } }
      );
      setEditMode(false);
      handleRefetch();
    } catch (err) {
      alert(`${response.status} error: ${response.data.message}`);
    }
  };

  const onEditButtonClick = () => {
    setEditMode(true);
  };

  const onDeleteButtonClick = async () => {
    try {
      await axios.delete(`${API_BASE_URL}/products/${productData.id}`, {
        headers: { Authorization: `Bearer ${accessToken}` },
      });
      alert("삭제 성공!");
      handleRefetch();
    } catch (err) {
      const response = err.response;
      alert(`${response.status} error: ${response.data.message}`);
    }
  };

  const handleProductNameChange = (e) => {
    setProductName(e.target.value);
  };

  const handleProductImageUrlChange = (e) => {
    setProductImageURL(e.target.value);
  };

  return (
    <ProductContainer>
      {editMode ? (
        <>
          <Column count={1}>{productData.id} </Column>
          <Column count={3}>
            <CustomInput
              value={productName}
              onChange={handleProductNameChange}
            />
          </Column>
          <Column count={1}>
            <ProductCategorySelect
              productCategory={productCategory}
              setProductCategory={setProductCategory}
            />
          </Column>
          <Column count={3}>
            <CustomInput
              value={productImageUrl}
              onChange={handleProductImageUrlChange}
            />
          </Column>
          <Column count={2}>
            <Button onClick={() => requestUpdate(productData.id)}>완료</Button>
            <Button onClick={() => setEditMode(false)}>취소</Button>
          </Column>
        </>
      ) : (
        <>
          <Column count={1}>{productData.id}</Column>
          <Column count={3}>{productName}</Column>
          <Column count={1}>{productCategory}</Column>
          <Column count={1}>
            <ProductImage src={productImageUrl} />
          </Column>
          <Column count={2}>
            <Button onClick={onEditButtonClick}>수정</Button>
            <Button onClick={onDeleteButtonClick}>삭제</Button>
          </Column>
        </>
      )}
    </ProductContainer>
  );
};

export default Product;

const ProductContainer = styled.section`
  display: flex;
  justify-content: space-around;
  align-items: center;
  width: 70%;
  margin: 0 auto;
  border-bottom: 1px solid black;
  padding: 1rem 0;
  gap: 1rem;
`;

const Column = styled.div`
  display: flex;
  gap: 1rem;
  width: ${({ count }) => count * 10}%;
`;

const CustomInput = styled.input`
  width: 100%;
  padding: 0.5rem;
`;

const ProductImage = styled.img`
  width: 100%;
  height: 100%;
  object-fit: contain;
`;
