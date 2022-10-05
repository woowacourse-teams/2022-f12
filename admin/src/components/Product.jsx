import axios from "axios";
import { useState } from "react";
import { API_BASE_URL } from "../constants/urls";

const Product = ({ productData, accessToken }) => {
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
    console.log(response);
    setEditMode(false);
  };

  const onEditButtonClick = () => {
    setEditMode(true);
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
    </div>
  );
};

export default Product;
