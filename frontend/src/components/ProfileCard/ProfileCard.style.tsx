import styled from 'styled-components';

export const Container = styled.div`
  display: flex;
  background-color: white;
  padding: 1rem;
  width: 360px;
  border-radius: 0.4rem;
  box-shadow: 4px 4px 10px ${({ theme }) => theme.colors.secondary};
`;

export const LeftSection = styled.div`
  position: relative;
  width: 20%;
`;

export const RightSection = styled.div`
  width: 80%;
`;

export const ProfileImageWrapper = styled.div`
  position: absolute;
  left: -5.5rem;
  top: 1.6rem;
  width: 9.5rem;
  height: 9.5rem;
  border-radius: 50%;
  overflow: hidden;
  box-shadow: 5px 5px 10px ${({ theme }) => theme.colors.secondary};
`;

export const ProfileImage = styled.img`
  width: 100%;
  height: 100%;
  object-fit: cover;
`;

export const UserInfoWrapper = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  margin-bottom: 0.9rem;
  gap: 0.4rem;
`;

export const UserNameWrapper = styled.div`
  display: flex;
  align-items: center;
  gap: 0.4rem;
`;

export const UserCareer = styled.div`
  display: flex;
  align-items: center;
  gap: 0.2rem;
`;

export const UserName = styled.span``;

export const InventoryWrapper = styled.div`
  display: flex;
  justify-content: space-between;
  height: 5rem;
`;

export const LeftButton = styled.button`
  border: none;
  background: none;
  font-size: 1.5rem;
`;
export const RightButton = styled.button`
  border: none;
  background: none;
  font-size: 1.5rem;
`;

export const ProfileViewButton = styled.button`
  border: none;
  width: 100%;
  height: 1.8rem;
  margin-top: 2rem;
  border-radius: 0.25rem;
  background-color: ${({ theme }) => theme.colors.primary};
`;

export const InventoryListWrapper = styled.div`
  overflow: hidden;
`;

export const InventoryList = styled.div<{ positionX: number }>`
  display: flex;
  align-items: flex-start;
  gap: 2rem;
  width: 200px;
  height: 100px;
  transform: ${({ positionX }) => `translateX(${positionX}px)`};
  transition: linear 0.2s;
`;

export const InventoryItem = styled.div`
  display: flex;
  flex-direction: column;
  padding: 0 2px;
`;

export const ProductImage = styled.img`
  width: 100%;
  height: 100%;
  object-fit: cover;
`;

export const ProductTitle = styled.p`
  font-size: 0.5rem;
`;

export const ProductImageWrapper = styled.div`
  width: 80px;
  height: 60px;
`;