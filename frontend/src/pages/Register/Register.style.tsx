import styled from 'styled-components';

export const Container = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2rem;
`;

export const Header = styled.header`
  font-size: 1.5rem;
`;

export const Title = styled.div`
  font-size: 1.2rem;
  align-items: center;
  justify-content: center;
  margin: 1rem 0 1.5rem 0;
`;

export const FlexWrapper = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
`;

export const FlexGapWrapper = styled.div`
  display: flex;
  flex-direction: column;
  gap: 1rem;
`;

export const SelectButton = styled.button<{ selected: boolean }>`
  background-color: ${({ selected }) => (selected ? '#f6bebe' : '#faf9f9')};
  width: 280px;
  height: 50px;
  border-radius: 8px;
  border: 1px solid #cfcfcf;
  font-size: 1rem;
  cursor: pointer;
  box-shadow: 8px 8px 3px -3px ${({ selected }) => (selected ? 'none' : '#cfcfcf')};

  transition: ease-in 0.2s;
`;

export const ConfirmButton = styled.button`
  background-color: #f6bebe;
  width: 100px;
  height: 50px;
  border-radius: 8px;
  border: 2px solid #f6bebe;
  font-size: 1rem;
  cursor: pointer;
  box-shadow: 8px 8px 3px -3px #cfcfcf;
`;

export const EditButton = styled(ConfirmButton)`
  background-color: #faf9f9;
`;

export const ConfirmInfo = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f6bebe;
  width: 280px;
  height: 50px;
  border-radius: 8px;
  border: 1px solid #cfcfcf;
  font-size: 1rem;
`;

export const FlexRowWrapper = styled.div`
  margin-top: 3rem;
  display: flex;
  gap: 1rem;
`;
