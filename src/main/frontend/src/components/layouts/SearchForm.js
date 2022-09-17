import { Button } from "react-bootstrap";
import Col from "react-bootstrap/Col";
import FloatingLabel from "react-bootstrap/FloatingLabel";
import Form from "react-bootstrap/Form";
import Row from "react-bootstrap/Row";

export const SearchForm = () => {
  return (
    <Form className="p-4">
      <Row className="g-4">
        <Col md>
          <FloatingLabel controlId="floatingInputGrid" label="Going to">
            <Form.Control type="text" placeholder="Gold Coast" />
          </FloatingLabel>
        </Col>
        <Col md>
          <FloatingLabel controlId="floatingInputGrid" label="Check In">
            <Form.Control type="date" name="check_in_date"></Form.Control>
          </FloatingLabel>
        </Col>
        <Col md>
          <FloatingLabel controlId="floatingInputGrid" label="Check Out">
            <Form.Control type="date" name="check_out_date"></Form.Control>
          </FloatingLabel>
        </Col>
        <Col md className="p-2">
          <Button variant="primary" type="submit">
            Submit
          </Button>
        </Col>
      </Row>
    </Form>
  );
};
