import styled from 'styled-components';

export const Container = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  width: 12rem;
  height: 12rem;
  border: 0.05rem solid black;
  border-radius: 0.375rem;
  padding: 1rem;
`;

export const Image = styled.img`
  width: 100%;
  height: 10rem;
  object-fit: cover;
`;

export const Wrapper = styled.div`
  display: flex;
  justify-content: space-between;
`;

export const Name = styled.p`
  font-size: 1rem;
`;
