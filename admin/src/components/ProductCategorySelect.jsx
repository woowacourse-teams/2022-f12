const ProductCategorySelect = ({ productCategory, setProductCategory }) => {
  const handleProductCategoryChange = (e) => {
    setProductCategory(e.target.value);
  };

  return (
    <select
      defaultValue={productCategory}
      onChange={handleProductCategoryChange}
      style={{ height: "100%" }}
    >
      <option value="keyboard">키보드</option>
      <option value="mouse">마우스</option>
      <option value="monitor">모니터</option>
      <option value="stand">스탠드</option>
      <option value="software">소프트웨어</option>
    </select>
  );
};

export default ProductCategorySelect;
