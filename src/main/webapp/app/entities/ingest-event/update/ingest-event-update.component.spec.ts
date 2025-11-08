import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IIngestRun } from 'app/entities/ingest-run/ingest-run.model';
import { IngestRunService } from 'app/entities/ingest-run/service/ingest-run.service';
import { IngestEventService } from '../service/ingest-event.service';
import { IIngestEvent } from '../ingest-event.model';
import { IngestEventFormService } from './ingest-event-form.service';

import { IngestEventUpdateComponent } from './ingest-event-update.component';

describe('IngestEvent Management Update Component', () => {
  let comp: IngestEventUpdateComponent;
  let fixture: ComponentFixture<IngestEventUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ingestEventFormService: IngestEventFormService;
  let ingestEventService: IngestEventService;
  let ingestRunService: IngestRunService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [IngestEventUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(IngestEventUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(IngestEventUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ingestEventFormService = TestBed.inject(IngestEventFormService);
    ingestEventService = TestBed.inject(IngestEventService);
    ingestRunService = TestBed.inject(IngestRunService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call IngestRun query and add missing value', () => {
      const ingestEvent: IIngestEvent = { id: 14502 };
      const ingestRun: IIngestRun = { id: 26594 };
      ingestEvent.ingestRun = ingestRun;

      const ingestRunCollection: IIngestRun[] = [{ id: 26594 }];
      jest.spyOn(ingestRunService, 'query').mockReturnValue(of(new HttpResponse({ body: ingestRunCollection })));
      const additionalIngestRuns = [ingestRun];
      const expectedCollection: IIngestRun[] = [...additionalIngestRuns, ...ingestRunCollection];
      jest.spyOn(ingestRunService, 'addIngestRunToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ingestEvent });
      comp.ngOnInit();

      expect(ingestRunService.query).toHaveBeenCalled();
      expect(ingestRunService.addIngestRunToCollectionIfMissing).toHaveBeenCalledWith(
        ingestRunCollection,
        ...additionalIngestRuns.map(expect.objectContaining),
      );
      expect(comp.ingestRunsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const ingestEvent: IIngestEvent = { id: 14502 };
      const ingestRun: IIngestRun = { id: 26594 };
      ingestEvent.ingestRun = ingestRun;

      activatedRoute.data = of({ ingestEvent });
      comp.ngOnInit();

      expect(comp.ingestRunsSharedCollection).toContainEqual(ingestRun);
      expect(comp.ingestEvent).toEqual(ingestEvent);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIngestEvent>>();
      const ingestEvent = { id: 10737 };
      jest.spyOn(ingestEventFormService, 'getIngestEvent').mockReturnValue(ingestEvent);
      jest.spyOn(ingestEventService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ingestEvent });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ingestEvent }));
      saveSubject.complete();

      // THEN
      expect(ingestEventFormService.getIngestEvent).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(ingestEventService.update).toHaveBeenCalledWith(expect.objectContaining(ingestEvent));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIngestEvent>>();
      const ingestEvent = { id: 10737 };
      jest.spyOn(ingestEventFormService, 'getIngestEvent').mockReturnValue({ id: null });
      jest.spyOn(ingestEventService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ingestEvent: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ingestEvent }));
      saveSubject.complete();

      // THEN
      expect(ingestEventFormService.getIngestEvent).toHaveBeenCalled();
      expect(ingestEventService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IIngestEvent>>();
      const ingestEvent = { id: 10737 };
      jest.spyOn(ingestEventService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ingestEvent });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ingestEventService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareIngestRun', () => {
      it('should forward to ingestRunService', () => {
        const entity = { id: 26594 };
        const entity2 = { id: 26980 };
        jest.spyOn(ingestRunService, 'compareIngestRun');
        comp.compareIngestRun(entity, entity2);
        expect(ingestRunService.compareIngestRun).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
