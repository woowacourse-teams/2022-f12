import axios from "axios";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { API_BASE_URL } from "../constants/urls";

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
        <select
          defaultValue={productCategory}
          onChange={(e) => {
            setProductCategory(e.target.value);
          }}
        >
          <option value="keyboard">키보드</option>
          <option value="mouse">마우스</option>
          <option value="monitor">모니터</option>
          <option value="stand">스탠드</option>
          <option value="software">소프트웨어</option>
        </select>
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
