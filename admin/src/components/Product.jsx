import axios from "axios";
import { useState } from "react";
import { API_BASE_URL } from "../constants/urls";

const Product = ({ productData, accessToken, handleRefetch }) => {
  const [editMode, setEditMode] = useState(false);
  const [nameInputValue, setNameInputValue] = useState(productData.name);
  const [categoryInputValue, setCategoryInputValue] = useState(
    productData.category
  );

  const requestUpdate = async (id) => {
    const response = await axios.patch(
      API_BASE_URL + "/products/" + id,
      {
        name: nameInputValue,
        imageUrl: productData.imageUrl,
        category: categoryInputValue,
      },
      { headers: { Authorization: "Bearer " + accessToken } }
    );
    setEditMode(false);
    handleRefetch();
  };

  const onEditButtonClick = () => {
    setEditMode(true);
  };

  const onDeleteButtonClick = async (e) => {
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

  const handleNameInputChange = (e) => {
    setNameInputValue(e.target.value);
  };

  const handleCategoryInputChange = (e) => {
    setCategoryInputValue(e.target.value);
  };

  return editMode ? (
    <div>
      <input value={nameInputValue} onChange={handleNameInputChange} />
      <select
        defaultValue={categoryInputValue}
        onChange={handleCategoryInputChange}
      >
        <option value="keyboard">키보드</option>
        <option value="mouse">마우스</option>
        <option value="monitor">모니터</option>
        <option value="stand">스탠드</option>
        <option value="software">소프트웨어</option>
      </select>
      <button onClick={() => requestUpdate(productData.id)}>완료</button>
    </div>
  ) : (
    <div>
      <span>{nameInputValue} </span>
      <span>{categoryInputValue} </span>
      <button onClick={onEditButtonClick}>수정</button>
      <button onClick={onDeleteButtonClick}>삭제</button>
    </div>
  );
};

export default Product;
