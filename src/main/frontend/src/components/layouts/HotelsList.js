import CardGroup from "react-bootstrap/CardGroup";
import Card from "react-bootstrap/Card";
import Col from "react-bootstrap/Col";
import Row from "react-bootstrap/Row";

export const HotelsList = () => {
  return (
    <Row xs={1} md={1} className="g-4">
      {Array.from({ length: 4 }).map((_, idx) => (
        <Col>
          <Card>
            <Card.Body>
              <Card.Title>Hotel Title</Card.Title>
              <Card.Subtitle className="mb-2 text-muted">
                Location
              </Card.Subtitle>
              <Card.Text>Maybe Some Description</Card.Text>
              <Card.Link href="#">Details</Card.Link>
              <Card.Link href="#">Link 2</Card.Link>
            </Card.Body>
          </Card>
        </Col>
      ))}
    </Row>
  );
};
