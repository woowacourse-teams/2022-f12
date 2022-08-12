import styled from 'styled-components';

export const Container = styled.div`
  display: flex;
  justify-content: space-between;
  width: 40rem;
`;

export const Item = styled.div<{ isComplete: boolean }>`
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  flex: 1;

  &::after {
    position: absolute;
    content: '';
    border: 2px solid #cfcfcf;
    width: 100%;
    top: 25px;
    left: 50%;
    z-index: 1;
  }
  ${({ isComplete }) =>
    isComplete &&
    `
      &::after {
        border: 2px solid #f6bebe;
        transition: ease-in 0.2s;
      };
      `}

  &:last-child::after {
    content: none;
  }
`;

export const Step = styled.div<{ isComplete: boolean; isCurrentStage: boolean }>`
  position: relative;
  z-index: 2;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 50px;
  height: 50px;
  border-radius: 50%;
  margin-bottom: 6px;
  font-size: 1.5rem;
  transition: ease-in 0.2s;
  border: 5px solid #cfcfcf;
  background-color: #faf9f9;

  ${({ isComplete, isCurrentStage }) =>
    isCurrentStage
      ? 'border: 5px solid #f6bebe;'
      : isComplete &&
        `
      border: 5px solid #f6bebe;
      background-color: #f6bebe;
      
      &::after {
        border: 2px solid #f6bebe;
        transition: ease-in 0.2s;
      };
      `}
`;

export const Title = styled.div`
  font-size: 1rem;
`;
