import axios from "axios";
import { useState } from "react";
import { API_BASE_URL } from "../constants/urls";
import ProductCategorySelect from "./ProductCategorySelect";

const Product = ({ productData, accessToken, handleRefetch }) => {
  const [editMode, setEditMode] = useState(false);
  const [productName, setProductName] = useState(productData.name);
  const [productCategory, setProductCategory] = useState(productData.category);

  const requestUpdate = async (id) => {
    try {
      await axios.patch(
        `${API_BASE_URL}/products/${id}`,
        {
          name: productName,
          imageUrl: productData.imageUrl,
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

  return editMode ? (
    <div>
      <span>{productData.id} </span>
      <input value={productName} onChange={handleProductNameChange} />
      <ProductCategorySelect
        productCategory={productCategory}
        setProductCategory={setProductCategory}
      />
      <button onClick={() => requestUpdate(productData.id)}>완료</button>
    </div>
  ) : (
    <div>
      <span>{productData.id}</span>
      <span>{productName} </span>
      <span>{productCategory} </span>
      <button onClick={onEditButtonClick}>수정</button>
      <button onClick={onDeleteButtonClick}>삭제</button>
    </div>
  );
};

export default Product;
