import React, { useCallback, useEffect, useMemo, useRef, useState } from 'react';
import { Translate } from 'react-jhipster';
import axios from 'axios';
import { Alert, Button, Col, Form, FormGroup, Input, Label, ListGroup, ListGroupItem, Row } from 'reactstrap';

type KafkaStreamEvent = {
  id: number;
  receivedAt: string;
  payload: string;
};

const MAX_EVENTS = 50;

const BooksEtlKafka = () => {
  const [message, setMessage] = useState('');
  const [events, setEvents] = useState<KafkaStreamEvent[]>([]);
  const [isListening, setIsListening] = useState(false);
  const [isPublishing, setIsPublishing] = useState(false);
  const [successKey, setSuccessKey] = useState<string | null>(null);
  const [errorKey, setErrorKey] = useState<string | null>(null);
  const eventSourceRef = useRef<EventSource | null>(null);

  const resetAlerts = useCallback(() => {
    setErrorKey(null);
    setSuccessKey(null);
  }, []);

  const addEvent = useCallback((payload: string) => {
    setEvents(previous => {
      const nextEvent: KafkaStreamEvent = {
        id: Date.now(),
        receivedAt: new Date().toISOString(),
        payload,
      };

      const updated = [nextEvent, ...previous];
      if (updated.length > MAX_EVENTS) {
        updated.pop();
      }
      return updated;
    });
  }, []);

  const publishMessage = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    resetAlerts();

    if (!message.trim()) {
      setErrorKey('booksEtlKafka.alerts.messageRequired');
      return;
    }

    setIsPublishing(true);
    try {
      await axios.post('api/books-etl-kafka/publish', null, {
        params: { message },
      });
      setSuccessKey('booksEtlKafka.alerts.publishSuccess');
      setMessage('');
    } catch (error) {
      setErrorKey('booksEtlKafka.alerts.publishError');
    } finally {
      setIsPublishing(false);
    }
  };

  const unregister = useCallback(() => {
    axios.get('api/books-etl-kafka/unregister').catch(() => undefined);
  }, []);

  const stopListening = useCallback(
    (shouldReport = true) => {
      const currentEventSource = eventSourceRef.current;
      if (currentEventSource) {
        currentEventSource.close();
        eventSourceRef.current = null;
        unregister();
        if (shouldReport) {
          setSuccessKey('booksEtlKafka.alerts.stopSuccess');
        }
      }
      setIsListening(false);
    },
    [unregister],
  );

  const startListening = useCallback(() => {
    if (isListening || eventSourceRef.current) {
      return;
    }

    resetAlerts();

    const eventSource = new EventSource(`${SERVER_API_URL}api/books-etl-kafka/register`);
    eventSource.onmessage = messageEvent => {
      addEvent(messageEvent.data);
    };
    eventSource.onerror = () => {
      stopListening(false);
      setErrorKey('booksEtlKafka.alerts.streamError');
    };

    eventSourceRef.current = eventSource;
    setIsListening(true);
    setSuccessKey('booksEtlKafka.alerts.listenSuccess');
  }, [addEvent, isListening, resetAlerts, stopListening]);

  useEffect(() => {
    return () => {
      stopListening(false);
    };
  }, [stopListening]);

  const formattedEvents = useMemo(
    () =>
      events.map(eventItem => ({
        ...eventItem,
        formattedTime: new Date(eventItem.receivedAt).toLocaleString(),
      })),
    [events],
  );

  return (
    <div>
      <h2 id="books-etl-kafka-heading" data-cy="booksEtlKafkaHeading">
        <Translate contentKey="booksEtlKafka.title" />
      </h2>
      <p className="lead">
        <Translate contentKey="booksEtlKafka.description" />
      </p>

      {successKey ? (
        <Alert color="success">
          <Translate contentKey={successKey} />
        </Alert>
      ) : null}

      {errorKey ? (
        <Alert color="danger">
          <Translate contentKey={errorKey} />
        </Alert>
      ) : null}

      <Row>
        <Col md="6" className="mb-4">
          <Form onSubmit={publishMessage} data-cy="booksEtlKafkaPublishForm">
            <FormGroup>
              <Label for="books-etl-kafka-message">
                <Translate contentKey="booksEtlKafka.publish.label" />
              </Label>
              <Input
                id="books-etl-kafka-message"
                type="textarea"
                rows={3}
                value={message}
                onChange={event => setMessage(event.target.value)}
                placeholder=""
              />
            </FormGroup>
            <Button color="primary" type="submit" disabled={isPublishing} data-cy="booksEtlKafkaPublishButton">
              <Translate contentKey="booksEtlKafka.publish.button" />
            </Button>
          </Form>
        </Col>
        <Col md="6">
          <div className="d-flex align-items-center mb-3">
            <Button color="success" onClick={startListening} disabled={isListening} className="me-2" data-cy="booksEtlKafkaStart">
              <Translate contentKey="booksEtlKafka.stream.start" />
            </Button>
            <Button color="secondary" onClick={() => stopListening()} disabled={!isListening} data-cy="booksEtlKafkaStop">
              <Translate contentKey="booksEtlKafka.stream.stop" />
            </Button>
          </div>
          <h4>
            <Translate contentKey="booksEtlKafka.stream.title" />
          </h4>
          <ListGroup data-cy="booksEtlKafkaEvents">
            {formattedEvents.length === 0 ? (
              <ListGroupItem>
                <Translate contentKey="booksEtlKafka.stream.empty" />
              </ListGroupItem>
            ) : (
              formattedEvents.map(eventItem => (
                <ListGroupItem key={eventItem.id}>
                  <div className="small text-muted">{eventItem.formattedTime}</div>
                  <div>{eventItem.payload}</div>
                </ListGroupItem>
              ))
            )}
          </ListGroup>
        </Col>
      </Row>
    </div>
  );
};

export default BooksEtlKafka;
