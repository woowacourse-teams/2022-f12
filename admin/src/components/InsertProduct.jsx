import axios from "axios";
import { useState } from "react";
import { API_BASE_URL } from "../constants/urls";

const InsertProduct = ({ accessToken }) => {
  const [productName, setProductName] = useState();
  const [productCategory, setProductCategory] = useState();
  const [productImageUrl, setProdcutImageURL] = useState();

  const requestInsertProduct = async () => {
    const response = await axios.post(
      API_BASE_URL + "/products",
      {
        name: productName,
        category: productCategory,
        imageUrl: productImageUrl,
      },
      { headers: { Authorization: "Bearer " + accessToken } }
    );
  };
  return (
    <div>
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
        <input
          onChange={(e) => {
            setProductCategory(e.target.value);
          }}
          type="text"
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
      <button onClick={requestInsertProduct}>제출</button>
    </div>
  );
};

export default InsertProduct;
