/*
 * Created on 2008/01/17
 */

package org.seasar.swing.builder;

import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import org.jdesktop.layout.GroupLayout.Group;
import org.jdesktop.layout.GroupLayout.ParallelGroup;
import org.jdesktop.layout.GroupLayout.SequentialGroup;

/**
 * @author Kaisei Hamamoto
 */

public class GroupLayoutBuilder {
    public abstract class ElementWrapper {
        public static final String ALIGNMENT_MESSAGE = "Alignment is only applicable to parallel groups.";
        public static final String BASELINE_MESSAGE = "Baseline is only applicable to sequential groups.";
        public static final String CONTAINER_GAP_MESSAGE = "Container gap is only applicable to sequential groups.";
        public static final String PREFERRED_GAP_MESSAGE = "Preferred gap is only applicable to sequential groups.";

        public ElementWrapper onlyIf(boolean condition) {
            return condition ? this : null;
        }

        protected ParallelGroup assertParallelGroup(Group group, String message) {
            if (!(group instanceof ParallelGroup)) {
                throw new IllegalStateException(message);
            }
            return (ParallelGroup) group;
        }

        protected SequentialGroup assertSequentialGroup(Group group,
                String message) {
            if (!(group instanceof SequentialGroup)) {
                throw new IllegalStateException(message);
            }
            return (SequentialGroup) group;
        }

        protected abstract void process(Group group);
    }

    public abstract class GroupWrapper extends ElementWrapper {
        private List<Object> elements = new ArrayList<Object>();

        protected GroupWrapper build(Object... args) {
            for (Object arg : args) {
                elements.add(arg);
            }
            return this;
        }

        protected abstract Group createGroup();

        public Group toGroup() {
            Group group = createGroup();
            for (Object e : elements) {
                if (e instanceof ElementWrapper) {
                    ((ElementWrapper) e).process(group);
                } else if (e instanceof Component) {
                    Component c = (Component) e;
                    if (group instanceof ParallelGroup) {
                        ((ParallelGroup) group).add(c);
                    } else if (group instanceof SequentialGroup) {
                        ((SequentialGroup) group).add(c);
                    }
                }
            }
            return group;
        }

        @Override
        protected void process(Group group) {
            if (group instanceof ParallelGroup) {
                ((ParallelGroup) group).add(toGroup());
            } else if (group instanceof SequentialGroup) {
                ((SequentialGroup) group).add(toGroup());
            }
        }
    }

    public class ParallelGroupWrapper extends GroupWrapper {
        private int selfAlignment;
        private boolean resizable;

        public ParallelGroupWrapper(int selfAlignment, boolean resizable) {
            this.selfAlignment = selfAlignment;
            this.resizable = resizable;
        }

        @Override
        protected Group createGroup() {
            return layout.createParallelGroup(selfAlignment, resizable);
        }
    }

    public class SequentialGroupWrapper extends GroupWrapper {
        @Override
        protected Group createGroup() {
            return layout.createSequentialGroup();
        }
    }

    public class AlignmentGroupWrapper extends ElementWrapper {
        private int alignment;
        private GroupWrapper wrapper;

        public AlignmentGroupWrapper(int alignment, GroupWrapper wrapper) {
            this.alignment = alignment;
            this.wrapper = wrapper;
        }

        @Override
        protected void process(Group group) {
            ParallelGroup g = assertParallelGroup(group, ALIGNMENT_MESSAGE);
            g.add(alignment, wrapper.toGroup());
        }
    }

    public class BaselineGroupWrapper extends ElementWrapper {
        private GroupWrapper wrapper;

        public BaselineGroupWrapper(GroupWrapper wrapper) {
            this.wrapper = wrapper;
        }

        @Override
        protected void process(Group group) {
            SequentialGroup g = assertSequentialGroup(group, BASELINE_MESSAGE);
            g.add(true, wrapper.toGroup());
        }
    }

    public class ComponentWrapper extends ElementWrapper {
        private Component component;
        private int alignment;
        private int min;
        private int pref;
        private int max;

        public ComponentWrapper(Component component, int alignment, int min,
                int pref, int max) {
            this.component = component;
            this.alignment = alignment;
            this.min = min;
            this.pref = pref;
            this.max = max;
        }

        @Override
        protected void process(Group group) {
            if (group instanceof ParallelGroup) {
                ParallelGroup g = (ParallelGroup) group;
                if (alignment != -1) {
                    g.add(alignment, component, min, pref, max);
                } else {
                    g.add(component, min, pref, max);
                }
            } else if (group instanceof SequentialGroup) {
                if (alignment != -1) {
                    assertParallelGroup(group, ALIGNMENT_MESSAGE);
                }
                SequentialGroup g = (SequentialGroup) group;
                g.add(component, min, pref, max);
            }
        }
    }

    public class BaselineComponentWrapper extends ElementWrapper {
        private Component component;
        private int min;
        private int pref;
        private int max;

        public BaselineComponentWrapper(Component component, int min, int pref,
                int max) {
            this.component = component;
            this.min = min;
            this.pref = pref;
            this.max = max;
        }

        @Override
        protected void process(Group group) {
            SequentialGroup g = assertSequentialGroup(group, BASELINE_MESSAGE);
            g.add(true, component, min, pref, max);
        }
    }

    public class GapWrapper extends ElementWrapper {
        private int min;
        private int pref;
        private int max;

        public GapWrapper(int size) {
            min = size;
            pref = size;
            max = size;
        }

        public GapWrapper(int min, int pref, int max) {
            this.min = min;
            this.pref = pref;
            this.max = max;
        }

        @Override
        protected void process(Group group) {
            if (group instanceof ParallelGroup) {
                ((ParallelGroup) group).add(min, pref, max);
            } else if (group instanceof SequentialGroup) {
                ((SequentialGroup) group).add(min, pref, max);
            }
        }
    }

    public class ContainerGapWrapper extends ElementWrapper {
        private int pref;
        private int max;

        public ContainerGapWrapper(int pref, int max) {
            this.pref = pref;
            this.max = max;
        }

        @Override
        protected void process(Group group) {
            SequentialGroup g = assertSequentialGroup(group,
                    CONTAINER_GAP_MESSAGE);
            g.addContainerGap(pref, max);
        }
    }

    public class PreferredGapWrapper extends ElementWrapper {
        private JComponent component1;
        private JComponent component2;
        private int placement;
        private boolean canGrow;
        private int pref;
        private int max;

        public PreferredGapWrapper(JComponent component1,
                JComponent component2, int placement, boolean canGrow,
                int pref, int max) {
            this.component1 = component1;
            this.component2 = component2;
            this.placement = placement;
            this.canGrow = canGrow;
            this.pref = pref;
            this.max = max;
        }

        @Override
        protected void process(Group group) {
            SequentialGroup g = assertSequentialGroup(group,
                    PREFERRED_GAP_MESSAGE);
            if (component1 != null && component2 != null) {
                g.addPreferredGap(component1, component2, placement, canGrow);
            } else {
                g.addPreferredGap(placement, pref, max);
            }
        }
    }

    private Container container;
    private GroupLayout layout;

    public GroupLayoutBuilder(Container container) {
        this(container, true, true);
    }

    public GroupLayoutBuilder(Container container, boolean autoCreateGaps,
            boolean autoCreateContainerGaps) {
        this.container = container;
        layout = new GroupLayout(container);
        layout.setAutocreateGaps(autoCreateGaps);
        layout.setAutocreateContainerGaps(autoCreateContainerGaps);
        container.setLayout(layout);
    }

    public Container getContainer() {
        return container;
    }

    public GroupLayout getLayout() {
        return layout;
    }

    public void setVerticalGroup(GroupWrapper group) {
        if (group != null) {
            layout.setVerticalGroup(group.toGroup());
        }
    }

    public void setHorizontalGroup(GroupWrapper group) {
        if (group != null) {
            layout.setHorizontalGroup(group.toGroup());
        }
    }

    public GroupWrapper par(Object... args) {
        return parWith(leading(), args);
    }

    public GroupWrapper parWith(int alignment, Object... args) {
        return new ParallelGroupWrapper(alignment, true).build(args);
    }

    public GroupWrapper parWithCenter(Object... args) {
        return new ParallelGroupWrapper(center(), true).build(args);
    }

    public GroupWrapper parWithTrailing(Object... args) {
        return new ParallelGroupWrapper(trailing(), true).build(args);
    }

    public GroupWrapper parWithBaseline(Object... args) {
        return new ParallelGroupWrapper(baseline(), true).build(args);
    }

    public GroupWrapper fixedPar(Object... args) {
        return fixedParWith(leading(), args);
    }

    public GroupWrapper fixedParWith(int alignment, Object... args) {
        return new ParallelGroupWrapper(alignment, false).build(args);
    }

    public GroupWrapper fixedParWithCenter(Object... args) {
        return new ParallelGroupWrapper(center(), false).build(args);
    }

    public GroupWrapper fixedParWithTrailing(Object... args) {
        return new ParallelGroupWrapper(trailing(), false).build(args);
    }

    public GroupWrapper fixedParWithBaseline(Object... args) {
        return new ParallelGroupWrapper(baseline(), false).build(args);
    }

    public GroupWrapper seq(Object... args) {
        return new SequentialGroupWrapper().build(args);
    }

    public AlignmentGroupWrapper alignment(int alignment, GroupWrapper wrapper) {
        return new AlignmentGroupWrapper(alignment, wrapper);
    }

    public BaselineGroupWrapper baseline(GroupWrapper wrapper) {
        return new BaselineGroupWrapper(wrapper);
    }

    public ComponentWrapper component(Component component) {
        return component(component, -1, -1, -1);
    }

    public ComponentWrapper component(Component component, int alignment) {
        return component(component, alignment, -1, -1, -1);
    }

    public ComponentWrapper component(Component component, int min, int pref,
            int max) {
        return component(component, -1, min, pref, max);
    }

    public ComponentWrapper component(Component component, int alignment,
            int min, int pref, int max) {
        return new ComponentWrapper(component, alignment, min, pref, max);
    }

    public BaselineComponentWrapper baseline(Component component) {
        return baseline(component, -1, -1, -1);
    }

    public BaselineComponentWrapper baseline(Component component, int min,
            int pref, int max) {
        return new BaselineComponentWrapper(component, min, pref, max);
    }

    public GapWrapper gap(int size) {
        return new GapWrapper(size);
    }

    public GapWrapper gap(int min, int pref, int max) {
        return new GapWrapper(min, pref, max);
    }

    public ContainerGapWrapper containerGap() {
        return containerGap(-1, -1);
    }

    public ContainerGapWrapper containerGap(int pref, int max) {
        return new ContainerGapWrapper(pref, max);
    }

    public PreferredGapWrapper preferredGap(int placement) {
        return preferredGap(placement, -1, -1);
    }

    public PreferredGapWrapper preferredGap(int placement, int pref, int max) {
        return new PreferredGapWrapper(null, null, placement, false, pref, max);
    }

    public PreferredGapWrapper preferredGap(JComponent component1,
            JComponent component2, int placement) {
        return preferredGap(component1, component2, placement, false);
    }

    public PreferredGapWrapper preferredGap(JComponent component1,
            JComponent component2, int placement, boolean canGrow) {
        return new PreferredGapWrapper(component1, component2, placement,
                canGrow, -1, -1);
    }

    public int leading() {
        return GroupLayout.LEADING;
    }

    public int center() {
        return GroupLayout.CENTER;
    }

    public int trailing() {
        return GroupLayout.TRAILING;
    }

    public int baseline() {
        return GroupLayout.BASELINE;
    }

    public int indent() {
        return LayoutStyle.INDENT;
    }

    public int related() {
        return LayoutStyle.RELATED;
    }

    public int unrelated() {
        return LayoutStyle.UNRELATED;
    }

    public int max() {
        return Short.MAX_VALUE;
    }
}
