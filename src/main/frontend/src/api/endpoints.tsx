const getAllHotels = async () => {
  const res = await fetch('/api/hotels');
  const data = await res.json();
  return data;
};

const endpoints = {
  getAllHotels,
};

export default endpoints;
