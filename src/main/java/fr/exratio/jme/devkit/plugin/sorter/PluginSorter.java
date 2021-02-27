/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package fr.exratio.jme.devkit.plugin.sorter;

import fr.exratio.jme.devkit.plugin.DevKitPlugin;
import fr.exratio.jme.devkit.plugin.sorter.DirectedGraph.DataNode;
import java.util.List;

public final class PluginSorter {

  private PluginSorter() {
  }

  public static List<DevKitPlugin> sort(Iterable<DevKitPlugin> candidates) {
    DirectedGraph<DevKitPlugin> graph = new DirectedGraph<>();

    for (DevKitPlugin candidate : candidates) {
      graph.add(candidate);
      for (DevKitPlugin dependency : candidate.getDependencies()) {
        graph.addEdge(candidate, dependency);
      }
    }

    try {
      return TopologicalOrder.createOrderedLoad(graph);
    } catch (CyclicGraphException e) {
      StringBuilder msg = new StringBuilder();
      msg.append("\nPlugin dependencies are cyclical!\n");
      msg.append("Dependency loops are:\n");
      for (DataNode<?>[] cycle : e.getCycles()) {
        msg.append("[");
        for (DataNode<?> node : cycle) {
          msg.append(node.getData().toString()).append(" ");
        }
        msg.append("]\n");
      }

      // throw new DependencyNotFoundException(msg.toString());
      throw new RuntimeException(msg.toString());

    }
  }

}
