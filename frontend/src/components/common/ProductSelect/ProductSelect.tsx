import ProductBar from '@/components/common/ProductBar/ProductBar';
import { useReducer } from 'react';

import * as S from '@/components/common/ProductSelect/ProductSelect.style';

type Props = {
  options: Product[];
  value: Product['id'];
  setValue: (id: Product['id']) => void;
};

function ProductSelect({ options, value, setValue }: Props) {
  const [isOpen, setOpen] = useReducer((isOpen: boolean) => !isOpen, false);
  const currentProduct = options.find(({ id }) => id === value);

  const handleProductSelect = (id: Product['id']) => {
    setValue(id);
    setOpen();
  };

  const otherOptions = options.filter(({ id }) => id !== value);

  const OptionListItems = otherOptions.map(({ id, name }) => (
    <S.Option key={id}>
      <S.PseudoButton onClick={() => handleProductSelect(id)}>
        <ProductBar name={name} isSelected={false} />
      </S.PseudoButton>
    </S.Option>
  ));

  return (
    <S.Container>
      <S.PseudoButton onClick={setOpen}>
        <ProductBar name={currentProduct.name} isSelected={true} />
      </S.PseudoButton>
      {isOpen && <S.OptionsList>{OptionListItems}</S.OptionsList>}
    </S.Container>
  );
}

export default ProductSelect;
