import styled from 'styled-components';

export const Container = styled.div`
  display: flex;
  justify-content: space-between;
  width: 40rem;
`;

export const Item = styled.div<{ step: number }>`
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  flex: 1;

  &::before {
    position: absolute;
    content: '';
    /* border: 2px solid #cfcfcf; */
    width: 100%;
    top: 25px;
    left: -50%;
    z-index: 2;
  }

  &::after {
    position: absolute;
    content: '';
    border: 2px solid #cfcfcf;
    width: 100%;
    top: 25px;
    left: 50%;
    z-index: 2;
  }

  &:first-child::before {
    content: none;
  }

  &:first-child::after {
    ${(props) => {
      if (props.step === 2 || props.step === 3)
        return `border: 2px solid #f6bebe;`;
    }};
  }

  &:last-child::after {
    content: none;
  }

  &:nth-child(2)::after {
    ${(props) => {
      if (props.step === 3) return `border: 2px solid #f6bebe;`;
    }};
  }

  &:first-child {
    .step {
      ${(props) => {
        if (props.step === 1) return `border: 5px solid #f6bebe;`;
        if (props.step === 2 || props.step === 3)
          return `border: 5px solid #f6bebe;
          background-color: #f6bebe`;
      }};
    }
  }

  &:nth-child(2) {
    .step {
      ${(props) => {
        if (props.step === 2) return `border: 5px solid #f6bebe;`;
        if (props.step === 3)
          return `border: 5px solid #f6bebe;
        background-color: #f6bebe`;
      }};
    }
  }

  &:last-child {
    .step {
      ${(props) => {
        if (props.step === 3) return `border: 5px solid #f6bebe`;
      }};
    }
  }
`;

export const Step = styled.div`
  position: relative;
  z-index: 3;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 50px;
  height: 50px;
  border-radius: 50%;
  border: 5px solid #cfcfcf;
  background-color: #faf9f9;
  margin-bottom: 6px;
  font-size: 1.5rem;
`;

export const Title = styled.div`
  font-size: 1rem;
`;
